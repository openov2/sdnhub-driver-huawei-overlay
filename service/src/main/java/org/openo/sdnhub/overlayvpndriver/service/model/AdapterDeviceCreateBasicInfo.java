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

package org.openo.sdnhub.overlayvpndriver.service.model;

import org.openo.sdno.overlayvpn.model.netmodel.localsite.AdapterDeviceReplaceInfo;
import org.openo.sdno.overlayvpn.verify.annotation.AString;

/**
 * Class of AdapterDevice Create Info.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 June 6, 2016
 */
public class AdapterDeviceCreateBasicInfo {

    /**
     * name
     */
    @AString(min = 0, max = 64)
    private String name;

    /**
     * ESN
     */
    @AString(require = true, min = 20, max = 20)
    private String esn;

    /**
     * name of organization
     */
    @AString(min = 0, max = 128)
    private String orgnizationName;

    /**
     * description
     */
    @AString(min = 0, max = 255)
    private String description;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the ESN.
     */
    public String getEsn() {
        return esn;
    }

    /**
     * @param esn The ESN to set.
     */
    public void setEsn(String esn) {
        this.esn = esn;
    }

    /**
     * @return Returns the orgnizationName.
     */
    public String getOrgnizationName() {
        return orgnizationName;
    }

    /**
     * @param orgnizationName The orgnizationName to set.
     */
    public void setOrgnizationName(String orgnizationName) {
        this.orgnizationName = orgnizationName;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (esn == null ? 0 : esn.hashCode());
        return result;
    }

    /**
     * Override equals Function.<br>
     * 
     * @param obj other Object
     * @return true if this object equals to other object
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null) {
            return false;
        }

        if(getClass() != obj.getClass()) {
            return false;
        }

        AdapterDeviceInfo other = (AdapterDeviceInfo)obj;
        if(esn == null) {
            if(other.getEsn() != null) {
                return false;
            }
        } else if(!esn.equals(other.getEsn())) {
            return false;
        }

        return true;
    }

}