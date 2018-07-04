/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.connector.Medium;


import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.Medium.util.Constants;
import org.wso2.carbon.connector.Medium.util.MediumConnectorUtil;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.xml.StringUtils;

import java.io.File;

/**
 * Connector to use template when creating a post
 */
public class MediumPostTemplateConnector extends AbstractConnector {



    /**
     * Create post using a template
     * @param messageContext
     * @throws ConnectException
     */
    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        Object postTypeObj = getParameter(messageContext, Constants.POST_TYPE_PARAM_NAME);
        Object paramListObj = getParameter(messageContext, Constants.PARAM_LIST_PARAM_NAME);

        String postType;
        String paramListObjString = Constants.EMPTY_JSON_ARRAY;

        if (postTypeObj != null) {
            postType = postTypeObj.toString();
        } else {
            throw new ConnectException("post_type Parameter is not set.");
        }

        if (paramListObj != null) {
            paramListObjString = paramListObj.toString();
        }


        String selectedTemplateFilePath = CarbonUtils.getCarbonRepository() + File.separator
                                            + Constants.RESOURCES_FOLDER_NAME + File.separator
                                            + Constants.MEDIUM_CONNECTOR_TEMPLATES_FOLDER_NAME
                                            + File.separator + postType;

        String templateValue = MediumConnectorUtil.getTemplateToString(selectedTemplateFilePath);

        if (StringUtils.isEmpty(templateValue)) {
            throw new ConnectException("No content in the template file provided.");
        }

        MediumConnectorUtil.replaceTemplateParameters(templateValue, paramListObjString);

        try {
            log.info("Medium sample connector received message :" + templateValue);
            /**Add your connector code here
             **/
        } catch (Exception e) {
            throw new ConnectException(e);
        }
        messageContext.setProperty("content", templateValue);
    }



}
