import requests
import json

url = "https://leetcode.cn/graphql"

payload = json.dumps({
    "operationName": None,
    "variables": {},
    "query": "{\n  contestUpcomingContests {\n    containsPremium\n    title\n    cardImg\n    titleSlug\n    description\n    startTime\n    duration\n    originStartTime\n    isVirtual\n    isLightCardFontColor\n    company {\n      watermark\n      __typename\n    }\n    __typename\n  }\n}\n"
})
headers = {
    'trace-context': '{"laneId":"STAGING.input_issue"}',
    'x-csrftoken': 'JT9Peb9qucMbJbyGMNd6eydu86zddUEUiwi33zoahMtX6uWWdajOPqijPfO1Trd7',
    'Cookie': 'csrftoken=JT9Peb9qucMbJbyGMNd6eydu86zddUEUiwi33zoahMtX6uWWdajOPqijPfO1Trd7; gr_user_id=93dbd22d-4cc4-4bff-955e-78726d60f445; _gid=GA1.2.2048079911.1737451084; _bl_uid=6amFd62w62j9pkinbzmIiI79OqOI; aliyungf_tc=a0c9fc790a5d4365eefa0eb8c6679cfb61ddc3834e3b11c11d9881180c535358; sl-session=6H8DDAthk2elq40Kn0UmDA; _did=web_261314603073C8; a2873925c34ecbd2_gr_session_id=46da0e44-60f3-4bf4-a627-f4eb98972689; a2873925c34ecbd2_gr_session_id_sent_vst=46da0e44-60f3-4bf4-a627-f4eb98972689; Hm_lvt_f0faad39bcf8471e3ab3ef70125152c3=1736324685,1737625485; Hm_lpvt_f0faad39bcf8471e3ab3ef70125152c3=1737625485; HMACCOUNT=41651A9F48670312; _gat=1; Hm_lvt_fa218a3ff7179639febdb15e372f411c=1736421123,1737625486; _gat_gtag_UA_131851415_1=1; _ga_PDVPZYN3CW=GS1.1.1737625487.18.1.1737625492.55.0.0; _ga=GA1.1.744130774.1736324685; Hm_lpvt_fa218a3ff7179639febdb15e372f411c=1737625493',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'content-type': 'application/json',
    'Accept': '*/*',
    'Host': 'leetcode.cn',
    'Connection': 'keep-alive'
}

response = requests.request("POST", url, headers=headers, data=payload)

print(response.text)
