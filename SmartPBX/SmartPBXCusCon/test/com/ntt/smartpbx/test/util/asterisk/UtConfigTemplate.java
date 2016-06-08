package com.ntt.smartpbx.test.util.asterisk;

import java.util.ArrayList;
import java.util.List;



public class UtConfigTemplate {
    
    
    public static List<StringBuffer> getTemplate(){
        
        List<StringBuffer> result = new ArrayList<StringBuffer>();
        
        result.add(new StringBuffer(""));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";sip.conf"));
        result.add(new StringBuffer("; ASTERISK_LOCAL_NETWORK_ADDRESS      %ASTERISK_LOCAL_NETWORK_ADDRESS%"));
        result.add(new StringBuffer("; ASTERISK_LOCAL_SUBNET_MASK          %ASTERISK_LOCAL_SUBNET_MASK%"));
        result.add(new StringBuffer("; ASTERISK_EXTERNAL_IP_ADDRESS        %ASTERISK_EXTERNAL_IP_ADDRESS%"));
        result.add(new StringBuffer("; INCLUDE_SIP_REG_OUTSIDENUMBER_CONF  %INCLUDE_SIP_REG_OUTSIDENUMBER_CONF%"));
        result.add(new StringBuffer("; INCLUDE_SIP_OUTSIDENUMBER_CONF      %INCLUDE_SIP_OUTSIDENUMBER_CONF%"));
        result.add(new StringBuffer("; ASTERISK_VPN_EXTERNAL_IP_ADDRESS    %ASTERISK_VPN_EXTERNAL_IP_ADDRESS%"));
        result.add(new StringBuffer("; ASTERISK_VPN_INTERN_IP_ADDRESS      %ASTERISK_VPN_INTERN_IP_ADDRESS%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";sip_user.conf"));
        result.add(new StringBuffer("; INCLUDE_SIP_EXTENSIONNUMBER_CONF %INCLUDE_SIP_EXTENSIONNUMBER_CONF%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";sip_内線番号.conf"));
        result.add(new StringBuffer(";"));
        result.add(new StringBuffer(";SIP_PEER_EXTENSION_NUMBER    %SIP_PEER_EXTENSION_NUMBER%    "));
        result.add(new StringBuffer(";SIP_PEER_NAME_PREFIX         %SIP_PEER_NAME_PREFIX%         "));
        result.add(new StringBuffer(";SIP_PEER_PASSWORD            %SIP_PEER_PASSWORD%            "));
        result.add(new StringBuffer(";BELONG_PICKUP_GROUP          %BELONG_PICKUP_GROUP%          "));
        result.add(new StringBuffer(";SIP_PEER_CALL_LIMIT          %SIP_PEER_CALL_LIMIT%          "));
        result.add(new StringBuffer(";SIP_VOIP_GW_REGISTER_NUMBER  %SIP_VOIP_GW_REGISTER_NUMBER%  "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";sip_外線番号.conf"));
        result.add(new StringBuffer("; SIP_OUTSIDE_PEER_NAME_PREFIX   %SIP_OUTSIDE_PEER_NAME_PREFIX%"));
        result.add(new StringBuffer("; SIP_PEER_OUTSIDE_NUMBER        %SIP_PEER_OUTSIDE_NUMBER%"));
        result.add(new StringBuffer("; FROM_USER_OUTSIDE_NUMBER       %FROM_USER_OUTSIDE_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_PASSWORD              %SIP_PEER_PASSWORD%"));
        result.add(new StringBuffer("; SIP_PEER_USER_NAME             %SIP_PEER_USER_NAME%"));
        result.add(new StringBuffer("; SIP_REGISTER_HOST              %SIP_REGISTER_HOST%"));
        result.add(new StringBuffer("; SIP_PEER_PORT                  %SIP_PEER_PORT%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";sip_reg外線番号.conf"));
        result.add(new StringBuffer("; SIP_PEER_OUTSIDE_NUMBER              %SIP_PEER_OUTSIDE_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTSIDE_NUMBER_WITHOUT_050  %SIP_PEER_OUTSIDE_NUMBER_WITHOUT_050%"));
        result.add(new StringBuffer("; SIP_PEER_USER_ID                     %SIP_PEER_USER_ID%"));
        result.add(new StringBuffer("; SIP_PEER_PASSWORD                    %SIP_PEER_PASSWORD%"));
        result.add(new StringBuffer("; SIP_OUTSIDE_PEER_NAME_PREFIX         %SIP_OUTSIDE_PEER_NAME_PREFIX%"));
        result.add(new StringBuffer("; SIP_PEER_PORT                        %SIP_PEER_PORT%"));
        result.add(new StringBuffer("; SIP_PEER_REGISTER_NUMBER             %SIP_PEER_REGISTER_NUMBER%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_globals.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_group.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_out.conf"));
        result.add(new StringBuffer("; INCLUDE_EXTENSION_OUTSIDENUMBER_CONF  %INCLUDE_EXTENSION_OUTSIDENUMBER_CONF%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_外線番号_in.conf"));
        result.add(new StringBuffer("; SIP_OUTSIDENUMBER                   %SIP_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; EXTENSIONNUMBER_FOR_OUTSIDENUMBER   %EXTENSIONNUMBER_FOR_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; SIP_WITHOUT_ZERO_OUTSIDENUMBER      %SIP_WITHOUT_ZERO_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_KIND               %SIP_PEER_OUTBAND_KIND%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_外線番号(IPV親番号)_in.conf"));
        result.add(new StringBuffer("; SIP_OUTSIDENUMBER                  %SIP_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; EXTENSIONNUMBER_FOR_OUTSIDENUMBER  %EXTENSIONNUMBER_FOR_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; SIP_WITHOUT_ZERO_OUTSIDENUMBER     %SIP_WITHOUT_ZERO_OUTSIDENUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_KIND              %SIP_PEER_OUTBAND_KIND%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_rule.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_outband.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_incoming.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_inrule.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_wait.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_user.conf"));
        result.add(new StringBuffer("; INCLUDE_PHYSICAL_EXTENSION_IN_OUT_CONF %INCLUDE_PHYSICAL_EXTENSION_IN_OUT_CONF%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_内線番号_in.conf"));
        result.add(new StringBuffer("; SIP_PEER_EXTENSION_NUMBER                     %SIP_PEER_EXTENSION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_FLAG_PARENT_OF_VOLLEY                %SIP_PEER_FLAG_PARENT_OF_VOLLEY%"));
        result.add(new StringBuffer("; SIP_PEER_FLAG_PARENT_OF_SLIDE                 %SIP_PEER_FLAG_PARENT_OF_SLIDE%"));
        result.add(new StringBuffer("; SIP_DIAL_GROUP_MEMER_OF_VOLLEY                %SIP_DIAL_GROUP_MEMER_OF_VOLLEY%"));
        result.add(new StringBuffer("; SIP_DIAL_GROUP_MEMER_OF_SLIDE                 %SIP_DIAL_GROUP_MEMER_OF_SLIDE%"));
        result.add(new StringBuffer("; SIP_PEER_ABSENCE_BEHAVIOR_TYPE                %SIP_PEER_ABSENCE_BEHAVIOR_TYPE%"));
        result.add(new StringBuffer("; SIP_PEER_FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL  %SIP_PEER_FORWARD_BEHAVIOR_TYPE_UNCONDITIONAL%"));
        result.add(new StringBuffer("; SIP_PEER_TRANSFER_TARGET                      %SIP_PEER_TRANSFER_TARGET%"));
        result.add(new StringBuffer("; SIP_PEER_CALL_TIME_OF_ABSENCE                 %SIP_PEER_CALL_TIME_OF_ABSENCE%"));
        result.add(new StringBuffer("; SIP_PEER_FORWARD_BEHAVIOR_TYPE_BUSY           %SIP_PEER_FORWARD_BEHAVIOR_TYPE_BUSY%"));
        result.add(new StringBuffer("; SIP_PEER_FORWARD_BEHAVIOR_TYPE_OUTSIDE        %SIP_PEER_FORWARD_BEHAVIOR_TYPE_OUTSIDE%"));
        result.add(new StringBuffer("; SIP_PEER_FORWARD_BEHAVIOR_TYPE_NO_ANSWER      %SIP_PEER_FORWARD_BEHAVIOR_TYPE_NO_ANSWER%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_START_TIME1                   %SIP_PEER_SINGLE_START_TIME1%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_DIAL_NUMBER1                  %SIP_PEER_SINGLE_DIAL_NUMBER1%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_START_TIME2                   %SIP_PEER_SINGLE_START_TIME2%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_DIAL_NUMBER2                  %SIP_PEER_SINGLE_DIAL_NUMBER2%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_CALL_END_TIME                 %SIP_PEER_SINGLE_CALL_END_TIME%"));
        result.add(new StringBuffer("; SIP_PEER_SINGLE_VOICEMAIL_FLAG                %SIP_PEER_SINGLE_VOICEMAIL_FLAG%"));
        result.add(new StringBuffer("; SIP_PEER_VOIP_GW_CALL                         %SIP_PEER_VOIP_GW_CALL%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_KIND                         %SIP_PEER_OUTBAND_KIND%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_内線番号_out.conf"));
        result.add(new StringBuffer("; SIP_PEER_EXTENSION_NUMBER              %SIP_PEER_EXTENSION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_PATTERN_TERMINAL_NUMBER       %SIP_PEER_PATTERN_TERMINAL_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_LOCATION_NUMBER               %SIP_PEER_LOCATION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_FLAG_NUMBER_LIMIT             %SIP_PEER_FLAG_NUMBER_LIMIT%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_NUMBER                %SIP_PEER_OUTBAND_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_KIND                  %SIP_PEER_OUTBAND_KIND%"));
        result.add(new StringBuffer("; SIP_PEER_PATTERN_LOCATION_NUMBER       %SIP_PEER_PATTERN_LOCATION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_NUMBER_DIALIN         %SIP_PEER_OUTBAND_NUMBER_DIALIN%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";extensions_内線番号(スマホ)_out.conf"));
        result.add(new StringBuffer("; SIP_PEER_EXTENSION_NUMBER            %SIP_PEER_EXTENSION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_PATTERN_TERMINAL_NUMBER     %SIP_PEER_PATTERN_TERMINAL_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_LOCATION_NUMBER             %SIP_PEER_LOCATION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_FLAG_NUMBER_LIMIT           %SIP_PEER_FLAG_NUMBER_LIMIT%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_NUMBER              %SIP_PEER_OUTBAND_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_KIND                %SIP_PEER_OUTBAND_KIND%"));
        result.add(new StringBuffer("; SIP_PEER_PATTERN_LOCATION_NUMBER     %SIP_PEER_PATTERN_LOCATION_NUMBER%"));
        result.add(new StringBuffer("; SIP_PEER_OUTBAND_NUMBER_DIALIN       %SIP_PEER_OUTBAND_NUMBER_DIALIN%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";voicemail.conf"));
        result.add(new StringBuffer("; VOICEMAIL_MAILBOX_SETTINGS         %VOICEMAIL_MAILBOX_SETTINGS%"));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer("; "));
        result.add(new StringBuffer(";features.conf"));
        result.add(new StringBuffer("; (なし)"));
        result.add(new StringBuffer(";"));

        return result;
    }

}


//(C) NTT Communications  2015  All Rights Reserved