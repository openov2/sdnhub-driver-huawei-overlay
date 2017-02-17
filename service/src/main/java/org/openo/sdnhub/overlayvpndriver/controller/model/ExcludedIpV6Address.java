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

package org.openo.sdnhub.overlayvpndriver.controller.model;

import org.openo.sdno.overlayvpn.verify.annotation.AString;

/**
 * Model class for ExcludedIpV6Address.<br/>
 *
 * @author
 * @version SDNHUB 0.5 02-Feb-2017
 */
public class ExcludedIpV6Address {

    @AString(require = false, min = 1, max = 128)
    private String startIp6Address;

    @AString(require = false, min = 1, max = 128)
    private String endIp6Address;

    public String getStartIp6Address() {
        return startIp6Address;
    }

    public void setStartIp6Address(String startIp6Address) {
        this.startIp6Address = startIp6Address;
    }

    public String getEndIp6Address() {
        return endIp6Address;
    }

    public void setEndIp6Address(String endIp6Address) {
        this.endIp6Address = endIp6Address;
    }

}
