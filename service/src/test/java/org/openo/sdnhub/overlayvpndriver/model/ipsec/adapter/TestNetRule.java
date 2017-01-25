/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

package org.openo.sdnhub.overlayvpndriver.model.ipsec.adapter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openo.sdnhub.overlayvpndriver.model.ipsec.adapter.NetRule;

public class TestNetRule {

	NetRule netRule = new NetRule(null, null, null, null, null);

	NetRule netRule1 = new NetRule();

	@Test
	public void checkGetPolicy() {
		String policy = "policy";
		netRule.setPolicy(policy);
		assertEquals(policy, netRule.getPolicy());
	}

	@Test
	public void checkGetSrcIp() {
		String srcIp = "1.1.1.1";
		netRule.setSrcIp(srcIp);
		assertEquals(srcIp, netRule.getSrcIp());
	}

	@Test
	public void checkGetSrcNetMask() {
		String srcNetMask = "srcNetMask";
		netRule.setSrcNetMask(srcNetMask);
		assertEquals(srcNetMask, netRule.getSrcNetMask());
	}

	@Test
	public void checkGetDesIp() {
		String desIp = "1.1.1.2";
		netRule.setDesIp(desIp);
		assertEquals(desIp, netRule.getDesIp());
	}

	@Test
	public void checkGetDesNetMask() {
		String desNetMask = "1.1.1.2";
		netRule.setDesNetMask(desNetMask);
		assertEquals(desNetMask, netRule.getDesNetMask());
	}
}
