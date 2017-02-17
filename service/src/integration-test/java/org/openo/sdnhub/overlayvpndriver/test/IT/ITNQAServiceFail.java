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

package org.openo.sdnhub.overlayvpndriver.test.IT;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.overlayvpndriver.test.mocoserver.NqaMockServer;
import org.openo.sdnhub.overlayvpndriver.test.mocoserver.NqaMocoServerHttps;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

public class ITNQAServiceFail {

    NqaMockServer nqaMockServer = new NqaMockServer();
    NqaMocoServerHttps nqaMoclServerHttps = new NqaMocoServerHttps();


    @Before
    public void setup() throws ServiceException {
        nqaMockServer.start();
    }

    @After
    public void tearDown() throws ServiceException {
        nqaMockServer.stop();
    }



    @Test
    public void testCreateFail() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/createNQA_Fail.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testCreateFail1() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/createNQA_Fail1.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testUpdateFail() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/updateNQA_Fail.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testUpdateFail1() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/updateNQA_Fail1.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testQueryFail() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/queryNQA_Fail.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testQueryFail1() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/queryNQA_Fail1.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testDeleteFail() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/deleteNQA_Fail.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    @Test
    public void testDeleteFail1() throws ServiceException {
        File createFile = new File("src/integration-test/resources/overlayvpndriver/deleteNQA_Fail1.json");
        HttpRquestResponse createHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(createFile));
        HttpResponse createResponse = new TestManager().execTestCase(createFile, new SuccessChecker());
        String response = createResponse.getData();
    }

    private class SuccessChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(response.getStatus() >= 400 && response.getStatus() <= 500) {
                return true;
            }
            return false;
        }
    }
}
