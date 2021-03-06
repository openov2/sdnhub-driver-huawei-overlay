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

package org.openo.sdnhub.overlayvpndriver.controller.consts;

/**
 * Enum for controller error codes.<br/>
 *
 * @author
 * @version SDNHUB 0.5 02-Feb-2017
 */
public enum ControllerErrorRsp {
    CTRL_ERR_SUBNET_SAME("0304390019"),
    CTRL_ERR_SUBNET_VNI_ALREADY_EXIST("0304390110");

    private String errCode;

    ControllerErrorRsp(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }
}
