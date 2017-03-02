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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdnhub.overlayvpndriver.controller.model.Ip;

public class IpTest {

    Ip ip = new Ip();

    /**
     * <br/>
     *
     * @throws Exception setup exception
     * @since SDNHUB 0.5
     */
    @Before
    public void setup() {
        ip.setIpMask("23");
        ip.setIpv4("12.12.12.12");
        ip.setPrefixLength("23");
        ip.setIpv6("::0000");
    }

    @Test
    public void testEqualIpMask() {

        Ip ip1 = new Ip();
        ip1.setIpMask("24");
        ip1.setIpv4("12.12.12.12");
        ip1.setPrefixLength("23");
        ip1.setIpv6("::0000");
        assertFalse(ip.equals(ip1));
    }

    @Test
    public void testEqualIpv4() {
        Ip ip1 = new Ip();
        ip1.setIpMask("23");
        ip1.setIpv4("12.12.12.13");
        ip1.setPrefixLength("23");
        ip1.setIpv6("::0000");
        assertFalse(ip.equals(ip1));
    }

    @Test
    public void testEqualPrefixLength() {
        Ip ip1 = new Ip();
        ip1.setIpMask("23");
        ip1.setIpv4("12.12.12.12");
        ip1.setPrefixLength("24");
        ip1.setIpv6("::0000");
        assertFalse(ip.equals(ip1));
    }

    @Test
    public void testEqualIpv6() {
        Ip ip1 = new Ip();
        ip1.setIpMask("23");
        ip1.setIpv4("12.12.12.12");
        ip1.setPrefixLength("23");
        ip1.setIpv6("::0001");
        assertFalse(ip.equals(ip1));
    }

    @Test
    public void testEqual() {

        Ip ip1 = new Ip();
        ip1.setIpMask("23");
        ip1.setIpv4("12.12.12.12");
        ip1.setPrefixLength("23");
        ip1.setIpv6("::0000");
        
        assertTrue(ip.equals(ip1));
        assertTrue(ip.equals(ip));
    }

    @Test
    public void testEqualAllNull() {
        Ip ip1 = new Ip();
        ip1.setIpMask(null);
        ip1.setIpv4(null);
        ip1.setPrefixLength(null);
        ip1.setIpv6(null);

        ip1.hashCode();
        ip.hashCode();
        
        assertFalse(ip.equals(ip1));
        assertFalse(ip.equals(null));
        assertFalse(ip.equals(new Object()));
    }

}
