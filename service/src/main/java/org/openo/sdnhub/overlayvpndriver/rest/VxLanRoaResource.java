/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdnhub.overlayvpndriver.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.sdnhub.overlayvpndriver.common.consts.CommonConst;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.overlayvpndriver.common.consts.CommonConst;
import org.openo.sdnhub.overlayvpndriver.common.util.RequestHeaderUtil;
import org.openo.sdnhub.overlayvpndriver.controller.model.Vni;
import org.openo.sdnhub.overlayvpndriver.controller.model.VxLanDeviceModel;
import org.openo.sdnhub.overlayvpndriver.result.ACDelResponse;
import org.openo.sdnhub.overlayvpndriver.sbi.impl.VxLanSvcImpl;
import org.openo.sdnhub.overlayvpndriver.translator.TranslateVxlanResponse;
import org.openo.sdnhub.overlayvpndriver.translator.VxlanConvert;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.vxlan.SbiNeVxlanInstance;
import org.openo.sdno.overlayvpn.result.FailData;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Restful interface for VxLan configuration.<br>
 *
 * @author
 * @version SDNHUB 0.5 Jun 19, 2017
 */
@Service
@Path("/sbi-vxlan/v1")
public class VxLanRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VxLanRoaResource.class);

    @Autowired
    private VxLanSvcImpl vxlanService = null;

    /**
     * Adds new Vxlan configuration using a specific Controller.<br>
     *
     * @param request HTTP request
     * @param ctrlUuidParam Controller UUID
     * @param vxLanInstanceList collection of VxLan configuration
     * @return ResultRsp object with VxLan added configuration status data
     * @throws WebApplicationException when add VxLan configuration fails
     * @throws ServiceException when input validation fails
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/batch-create-vxlan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<SbiNeVxlanInstance> createVxlan(@Context HttpServletRequest request,
                                                     @HeaderParam(CommonConst.CTRL_HEADER_PARAM) String ctrlUuidParam,
                                                     List<SbiNeVxlanInstance> vxLanInstanceList)
            throws WebApplicationException, ServiceException {
        long beginTime = System.currentTimeMillis();
        String ctrlUuid = RequestHeaderUtil.readControllerUUID(ctrlUuidParam);

        UuidUtil.validate(ctrlUuid);

        if(CollectionUtils.isEmpty(vxLanInstanceList)) {
            throw new ServiceException(ErrorCode.OVERLAYVPN_PARAMETER_INVALID);
        }

        ResultRsp<SbiNeVxlanInstance> totalResult = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS);
        List<SbiNeVxlanInstance> vxlanInstanceList = VxlanConvert.checkInputCreateVxlan(vxLanInstanceList);
        Map<String, List<VxLanDeviceModel>> netVxlanDeviceModelMap =
                VxlanConvert.convertVxlanInsToNetVxlanDeviceModel(vxLanInstanceList);
        Map<String, List<SbiNeVxlanInstance>> deviceIdToVxlaninsMap =
                VxlanConvert.divideVxlanInsByDeviceId(vxlanInstanceList);

        List<SbiNeVxlanInstance> succVxlanInstances = new ArrayList<>();
        List<FailData<SbiNeVxlanInstance>> faildata = new ArrayList<>();
        for(Map.Entry<String, List<VxLanDeviceModel>> entry : netVxlanDeviceModelMap.entrySet()) {
            String deviceId = entry.getKey();
            ResultRsp<List<VxLanDeviceModel>> createResult =
                    VxLanSvcImpl.createVxLanByDevice(ctrlUuid, deviceId, entry.getValue());

            List<SbiNeVxlanInstance> correNbiVxlanInsList = deviceIdToVxlaninsMap.get(deviceId);
            if(createResult.isSuccess()) {
                TranslateVxlanResponse.translateVxlanId(vxLanInstanceList, deviceId, createResult.getData());
                succVxlanInstances.addAll(correNbiVxlanInsList);
            } else {
                LOGGER.error("create vxlan for deviceId:" + deviceId + " failed.");
                for(SbiNeVxlanInstance failIns : correNbiVxlanInsList) {
                    faildata.add(new FailData<SbiNeVxlanInstance>(createResult.getErrorCode(),
                            createResult.getMessage(), failIns));
                }
                totalResult.setErrorCode(ErrorCode.OVERLAYVPN_FAILED);
            }
        }

        totalResult.setSuccessed(succVxlanInstances);
        totalResult.setFail(faildata);
        LOGGER.debug("Create VXLAN costed time = " + (System.currentTimeMillis() - beginTime));
        return totalResult;
    }

    /**
     * Deletes Vxlan configuration using a specific Controller.<br/>
     *
     * @param request HTTP request
     * @param deviceId specific device id
     * @param ctrlUuidParam Controller UUID
     * @param vxlanInstanceList collection of VxLan configuration
     * @return ResultRsp object with deleted VxLan configuration status data
     * @throws ServiceException when input validation fails
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/device/{deviceid}/batch-delete-vxlan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public ResultRsp<SbiNeVxlanInstance> batchDeleteVxlan(@Context HttpServletRequest request,
                                                          @PathParam(CommonConst.DEVICE_ID_PATH_PARAM) String deviceId,
                                                          @HeaderParam(CommonConst.CTRL_HEADER_PARAM) String ctrlUuidParam,
                                                          List<SbiNeVxlanInstance> vxlanInstanceList) throws ServiceException {

        String ctrlUuid = RequestHeaderUtil.readControllerUUID(ctrlUuidParam);

        if(CollectionUtils.isEmpty(vxlanInstanceList)) {
            throw new ServiceException(ErrorCode.OVERLAYVPN_PARAMETER_INVALID);
        }

        long beginTime = System.currentTimeMillis();
        UuidUtil.validate(ctrlUuid);
        ResultRsp<SbiNeVxlanInstance> totalResult = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS);
        Map<String, List<VxLanDeviceModel>> deviceIdToDeviceModelMap =
                VxlanConvert.convertVxlanInsToNetVxlanDeviceModel(vxlanInstanceList);

        LOGGER.debug("vxlanDeviceModelMap:" + JsonUtil.toJson(deviceIdToDeviceModelMap));
        Map<String, List<SbiNeVxlanInstance>> deviceIdToVxlanInsMap =
                VxlanConvert.divideVxlanInsByDeviceId(vxlanInstanceList);
        LOGGER.debug("deviceIdToVxlanInsMap:" + JsonUtil.toJson(deviceIdToVxlanInsMap));

        List<SbiNeVxlanInstance> succVxlanInstances = new ArrayList<>();
        List<FailData<SbiNeVxlanInstance>> failDatas = new ArrayList<>();
        for(Map.Entry<String, List<VxLanDeviceModel>> entry : deviceIdToDeviceModelMap.entrySet()) {
            List<VxLanDeviceModel> delVxlanDeviceModels = entry.getValue();
            List<String> ids = new ArrayList<>(CollectionUtils.collect(delVxlanDeviceModels, new Transformer() {

                @Override
                public Object transform(Object input) {
                    return ((VxLanDeviceModel)input).getUuid();
                }
            }));

            ResultRsp<ACDelResponse> delResult = VxLanSvcImpl.deleteVxlanByDevice(ctrlUuid, deviceId, ids);
            List<SbiNeVxlanInstance> vxlanInstances = deviceIdToVxlanInsMap.get(deviceId);
            if(delResult.isSuccess()) {
                succVxlanInstances.addAll(vxlanInstances);
            } else {
                for(SbiNeVxlanInstance ins : vxlanInstances) {
                    failDatas.add(
                            new FailData<SbiNeVxlanInstance>(delResult.getErrorCode(), delResult.getMessage(), ins));
                }
            }
        }
        totalResult.setSuccessed(succVxlanInstances);
        totalResult.setFail(failDatas);

        if(CollectionUtils.isNotEmpty(failDatas)) {
            totalResult.setErrorCode(ErrorCode.OVERLAYVPN_FAILED);
        }
        LOGGER.warn("vxlan delete cost time = " + (System.currentTimeMillis() - beginTime));
        return totalResult;
    }

    /**
     * Queries Vxlan configuration using a specific Controller.<br/>
     *
     * @param request HTTP request
     * @param ctrlUuidParam Controller UUID
     * @param vxlanInstanceList collection of VxLan configuration
     * @return ResultRsp object with VxLan queried configuration status data
     * @throws ServiceException when input validation fails
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/batch-query-vxlan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<SbiNeVxlanInstance> queryVxlan(@Context HttpServletRequest request,
                                                    @HeaderParam(CommonConst.CTRL_HEADER_PARAM) String ctrlUuidParam,
                                                    List<SbiNeVxlanInstance> vxlanInstanceList)
            throws ServiceException {

        String ctrlUuid = RequestHeaderUtil.readControllerUUID(ctrlUuidParam);
        long beginTime = System.currentTimeMillis();

        if(CollectionUtils.isEmpty(vxlanInstanceList)) {
            throw new ServiceException(ErrorCode.OVERLAYVPN_PARAMETER_INVALID);
        }

        ResultRsp<SbiNeVxlanInstance> totalResult = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS);
        Map<String, List<SbiNeVxlanInstance>> deviceIdToDeviceModelMap = VxLanSvcImpl.groupByDeviceId(vxlanInstanceList);
        for(Map.Entry<String, List<SbiNeVxlanInstance>> entry : deviceIdToDeviceModelMap.entrySet()) {
            String deviceId = entry.getKey();
            List<VxLanDeviceModel> acExistVxlanModels = VxLanSvcImpl.queryVxlanByDevice(ctrlUuid, deviceId).getData();

            if(CollectionUtils.isEmpty(acExistVxlanModels)) {
                for(SbiNeVxlanInstance sbiNeVxlanInstance : entry.getValue()) {
                    FailData<SbiNeVxlanInstance> failData = new FailData<>(
                            ErrorCode.OVERLAYVPN_FAILED, ErrorCode.COMMON_CONFIG_NOT_EXIST, sbiNeVxlanInstance);
                    if(totalResult.getFail() == null) {
                        totalResult.setFail(new ArrayList<>());
                    }
                    totalResult.getFail().add(failData);
                }
            } else {
                VxLanSvcImpl.findData(totalResult, entry, acExistVxlanModels);
            }
        }
        if(CollectionUtils.isNotEmpty(totalResult.getFail())) {
            totalResult.setErrorCode(ErrorCode.OVERLAYVPN_FAILED);
        }
        LOGGER.debug("vxlan create cost time = " + (System.currentTimeMillis() - beginTime));
        return totalResult;
    }

    /**
     * Updates Vxlan configuration using a specific Controller.<br/>
     *
     * @param request HTTP request
     * @param ctrlUuidParam Controller UUID
     * @param vxLanInstanceList collection of VxLan configuration
     * @return ResultRsp object with VxLan updated configuration status data
     * @throws ServiceException when input validation fails
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/batch-update-vxlan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<SbiNeVxlanInstance> updateVxlan(@Context HttpServletRequest request,
                                                     @HeaderParam(CommonConst.CTRL_HEADER_PARAM) String ctrlUuidParam,
                                                     List<SbiNeVxlanInstance> vxLanInstanceList)
            throws ServiceException {

        String ctrlUuid = RequestHeaderUtil.readControllerUUID(ctrlUuidParam);
        long beginTime = System.currentTimeMillis();
        if(CollectionUtils.isEmpty(vxLanInstanceList)) {
            throw new ServiceException(ErrorCode.OVERLAYVPN_PARAMETER_INVALID);
        }

        ResultRsp<SbiNeVxlanInstance> totalResult = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS);
        Map<String, List<VxLanDeviceModel>> deviceIdToDeviceModelMap =
                VxlanConvert.convertVxlanInsToNetVxlanDeviceModel(vxLanInstanceList);
        LOGGER.debug("vxlanDeviceModelMap:" + JsonUtil.toJson(deviceIdToDeviceModelMap));
        Map<String, List<SbiNeVxlanInstance>> deviceIdToVxlanInsMap =
                VxlanConvert.divideVxlanInsByDeviceId(vxLanInstanceList);
        LOGGER.debug("deviceIdToVxlanInsMap:" + JsonUtil.toJson(deviceIdToVxlanInsMap));

        List<SbiNeVxlanInstance> succVxlanInstances = new ArrayList<>();
        List<FailData<SbiNeVxlanInstance>> failDatas = new ArrayList<>();
        for(Map.Entry<String, List<VxLanDeviceModel>> entry : deviceIdToDeviceModelMap.entrySet()) {
            String deviceId = entry.getKey();
            ResultRsp<List<VxLanDeviceModel>> createResult =
                    VxLanSvcImpl.updateVxlanByDevice(ctrlUuid, deviceId, entry.getValue());
            List<SbiNeVxlanInstance> currNbiVxlanInsList = deviceIdToVxlanInsMap.get(deviceId);
            if(createResult.isSuccess()) {
                LOGGER.debug("deviceId:" + deviceId + " success");
                succVxlanInstances.addAll(currNbiVxlanInsList);
            } else {
                LOGGER.debug("deviceId:" + deviceId + " fail");
                for(SbiNeVxlanInstance failVxlanIns : currNbiVxlanInsList) {
                    failDatas.add(new FailData<SbiNeVxlanInstance>(createResult.getErrorCode(),
                            createResult.getMessage(), failVxlanIns));
                }
            }
        }

        totalResult.setSuccessed(succVxlanInstances);
        totalResult.setFail(failDatas);
        if(CollectionUtils.isNotEmpty(failDatas)) {
            totalResult.setErrorCode(ErrorCode.OVERLAYVPN_FAILED);
        }

        LOGGER.debug("vxlan create cost time = " + (System.currentTimeMillis() - beginTime));
        return totalResult;
    }
}
