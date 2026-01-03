import json

import requests

url = "https://codeforces.com/api/contest.list"

payload = {}
headers = {
    'priority': 'u=0, i',
    'sec-ch-ua-arch': '"x86"',
    'sec-ch-ua-bitness': '"64"',
    'sec-ch-ua-full-version': '"131.0.6778.86"',
    'sec-ch-ua-full-version-list': '"Google Chrome";v="131.0.6778.86", "Chromium";v="131.0.6778.86", "Not_A Brand";v="24.0.0.0"',
    'sec-ch-ua-model': '""',
    'sec-ch-ua-platform-version': '"15.0.0"',
    'sec-fetch-user': '?1',
    'upgrade-insecure-requests': '1',
    'Cookie': 'X-User-Sha1=04e324b6da3ddb116e420031cb86d5ff4b4e3469; _gid=GA1.2.1571391696.1732670797; pow=594679c208257ea910f19849122b0b627a08989d; X-User=dacb64f14cefc6dcca0866d4383c097effbf750a657da1c1fd8fafbdd9f90badbbb5feeb6d77eecf; JSESSIONID=E20BFDC18DBE55FF64795FCFAD70FEB3; 39ce7=CF7osZhh; evercookie_png=6h0p0ewq4umn50r9uo; evercookie_etag=6h0p0ewq4umn50r9uo; evercookie_cache=6h0p0ewq4umn50r9uo; 70a7c28f3de=6h0p0ewq4umn50r9uo; cf_clearance=1wtSTR_vxAD5_gBcDlaHqF8UB1lPlLfnmRNYlRYJBHQ-1732781214-1.2.1.1-X4W1yfvG6sl_0wVFVVyJMNVrPX2_t3l5edMIU1Rg0Jv81D9XvCrBa22J.cdWVRl_YsbwHTbtm8m8nsKVHqGYmo3EiN3dj0RpJQX4CTyivgXG.kUlMD7N8EqRCcfGnjB4TEVbr_UQKAisH24csm5Q545d5Hup7PEU2xXqlNkTUUTxi1AoTlTIVejNlabR9NJ5C3Bmn1X4v4q76W_WRYovdSfK8GCVn0FBY5Gkm5s4mw0q_Te9OnxNZQkIV7p4RS5CzbpszBvKDUtpHZYbAUHjzgHjEJHiR6mbcThNzxryK_0yAJIkUI5STUmJ0.kkq8aDj8J_4s9K.qd0OWyPePXoGGPw2gR6jTrH2Em5jPkdtMPNePW3tmvwP2E4RS4u6QrHbohe1er2Ctu0CfJF23Ue5A; _ga_K230KVN22K=GS1.1.1732781209.61.1.1732781698.0.0.0; _ga=GA1.1.273042412.1728735683; lastOnlineTimeUpdaterInvocation=1732796577624; JSESSIONID=AAF758A4EEBA5813D75AAEA1FC0D8607; 39ce7=CF2Z87o0',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'codeforces.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)
# # 将JSON字符串解析为Python字典
# data_dict = json.loads(response.text)
#
# # 遍历结果列表，提取每个竞赛的信息
# for contest in data_dict["result"]:
#     contest_id = contest["id"]
#     contest_name = contest["name"]
#     contest_type = contest["type"]
#     contest_phase = contest["phase"]
#     start_time_seconds = contest["startTimeSeconds"]
#     durationSeconds = contest["durationSeconds"]
#     if type == "CF":
#         print(f"竞赛ID: {contest_id}")
#         print(f"竞赛名称: {contest_name}")
#         print(f"竞赛类型: {contest_type}")
#         print(f"开始时间（秒）: {start_time_seconds}")
#         print("-" * 30)
