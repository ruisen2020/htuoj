import requests
import sys
url = "https://ac.nowcoder.com/acm/calendar/contest?token=&month="+sys.argv[1]+"&_=1737622616182"

payload={}
headers = {
    'priority': 'u=1, i',
    'trace-context': '{"laneId":"STAGING.input_issue"}',
    'x-requested-with': 'XMLHttpRequest',
    'Cookie': 'gr_user_id=eaf7fa62-e7d3-4f13-95ce-8471ce270896; NOWCODERCLINETID=307FF051FAC631D0BEB5C8C7D370577E; NOWCODERUID=AF5DE3E324F42085E15AE574E4A21923; isAgreementChecked=true; c196c3667d214851b11233f5c17f99d5_gr_last_sent_cs1=414186323; acw_tc=888c66b3d83ea1e03fc30507344d361885096dcf5526e453918e8c34b0bf8db9; c196c3667d214851b11233f5c17f99d5_gr_session_id=dee884a6-a665-4eb4-8d82-bf464627037d; c196c3667d214851b11233f5c17f99d5_gr_session_id_sent_vst=dee884a6-a665-4eb4-8d82-bf464627037d; Hm_lvt_a808a1326b6c06c437de769d1b85b870=1736414696,1737622470; HMACCOUNT=41651A9F48670312; _did=web_466664985483713A; Hm_lpvt_a808a1326b6c06c437de769d1b85b870=1737622563',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'ac.nowcoder.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)
