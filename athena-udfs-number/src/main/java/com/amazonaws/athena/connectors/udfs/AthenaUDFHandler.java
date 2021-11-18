/*-
 * #%L
 * athena-udfs
 * %%
 * Copyright (C) 2019 Amazon Web Services
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.amazonaws.athena.connectors.udfs;

import com.amazonaws.athena.connector.lambda.handlers.UserDefinedFunctionHandler;
import com.amazonaws.athena.connector.lambda.security.CachableSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient;
import com.google.common.annotations.VisibleForTesting;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class AthenaUDFHandler
        extends UserDefinedFunctionHandler
{
    private static final String SOURCE_TYPE = "athena_common_udfs";

    private final CachableSecretsManager cachableSecretsManager;

    public AthenaUDFHandler()
    {
        this(new CachableSecretsManager(AWSSecretsManagerClient.builder().build()));
    }

    @VisibleForTesting
    AthenaUDFHandler(CachableSecretsManager cachableSecretsManager)
    {
        super(SOURCE_TYPE);
        this.cachableSecretsManager = cachableSecretsManager;
    }

    /** Numberparse
     * 
     * 
     * 
     * @param number the rawnumber to parse
     * @param region the region of number
     * @return regionNumber string
     */
    public String pnp_region_number(String number, String region)
    {
        if (number == null) {
            return null;
        }
        if (region == null) {
            return "," + number;
        }
        
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        PhoneNumber pnp = null;
        String defaultResult = region + "," + number;

        try {
            pnp = util.parse(number.toString(), region.toString());
        } 
        catch (NumberParseException e) {
            pnp = null;
        }

        if (pnp == null) {
            return defaultResult;
        }

        String parsedRegion = util.getRegionCodeForNumber(pnp);
        if (parsedRegion == null) {
            parsedRegion = region;
        }

        String parsedE164 = util.format(pnp, PhoneNumberFormat.E164);

        if (util.isValidNumber(pnp)) { // parsed region, parsed number
            return parsedRegion + "," + parsedE164.replace("+", "");
        } 
        else {
            if (util.isPossibleNumber(pnp)) { // parsed region, original number
                return parsedRegion + "," + number;
            } 
            else { // original region, original number
                return defaultResult;
            }
        }
    }
}
