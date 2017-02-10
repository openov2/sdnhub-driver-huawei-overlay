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

package org.openo.sdnhub.overlayvpndriver.common.consts;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNHUB 0.5 02-Feb-2017
 */
public class CommonConst {

    public static final String APPLICATION_JSON = "application/json";

    public static final int FAILED = -1;

    public static final String ETHCONFIG_LIST = "ethInterfaceConfig";

    public static final String ETHCONFIG_INTERFACENAME = "name";
    
    public static final String CTRL_HEADER_PARAM = "X-Driver-Parameter";
    
    public static final String DEVICE_ID_PATH_PARAM = "deviceid";

    public static final String DEVICE_IDS_PATH_PARAM = "deviceIds";

    public static final String DEVICE_UUID_PATH_PARAM = "deviceuuid";

    public static final String ESN_QUERY_PARAM = "esn";
    
    public static final String STATIC_ROUTE_CONFIGS = "staticRouteConfigs";

    // Device Restful interface URL Path Constants
    public static final String DEVICE_RESTFUL_PATH = "/sbi-localsite/v1";

    public static final String DEVICE_RESTFUL_OPERATION_PATH = "/devices";
    public static final String DEVICE_RESTFUL_UPDATE_OPERATION_PATH = "/deviceid/{deviceuuid}";
}
