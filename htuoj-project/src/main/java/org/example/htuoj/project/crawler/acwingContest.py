import re

import requests
import sys
import io

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='UTF-8')

url = "https://www.acwing.com/activity/1/competition/"

payload = {}
headers = {
    'Sec-Fetch-User': '?1',
    'Upgrade-Insecure-Requests': '1',
    'Cookie': 'csrftoken=iBs87q5CKkhS902vUhRHulYLvdfMaCAlwTDBVxPdIymsoMBShUAC5SSSIQYX9VZh; sessionid=3xegep850j6e7ucziumyg2ccru4ofaw5; p_h5_upload_u=ED6D16A5-1226-4F9B-9986-6625EEE35FD5; csrftoken=iBs87q5CKkhS902vUhRHulYLvdfMaCAlwTDBVxPdIymsoMBShUAC5SSSIQYX9VZh',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'www.acwing.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

# print(response.text)
html = response.text
# 定义正则表达式模式，用于匹配整个div块（包含内部所有信息）
div_pattern = r'<div class="col-md-11".*?>\s*(<a href=".*?".*?>.*?</a>)\s*</div>'
div_matches = re.findall(div_pattern, html, re.DOTALL)

# 用于存储所有活动信息的列表
all_activities = []

# 遍历每个匹配到的div块内容，提取其中的活动信息
for div_content in div_matches:

    # print(div_content)
    activity_info = {}
    try:
        activity_info['title'] = re.search(r'<span class="activity_title">(.*?)</span>', div_content, re.DOTALL).group(
            1).strip()
    except AttributeError:
        activity_info['title'] = "未获取到活动标题"
    try:
        activity_info['status'] = re.search(r'<span class="btn btn-warning activity_status">(.*?)</span>', div_content,
                                            re.DOTALL).group(1).strip()
    except AttributeError:
        activity_info['status'] = "未获取到活动状态"
    try:
        activity_info['abstract'] = re.search(r'<span class="activity_abstract">(.*?)</span>', div_content,
                                              re.DOTALL).group(1).strip()
    except AttributeError:
        activity_info['abstract'] = "未获取到活动摘要"
    try:
        activity_info['participant_num'] = re.search(r'<span class="activity_td">(.*?)</span>', div_content,
                                                     re.DOTALL).group(1).strip()
    except AttributeError:
        activity_info['participant_num'] = "未获取到参与人数"
    try:
        activity_info['start_time'] = re.search(
            r'<span class="activity_td">(.*?)</span>.*?开始时间.*?<span class="activity_td">(.*?)</span>', div_content,
            re.DOTALL).group(2).strip()
    except AttributeError:
        activity_info['start_time'] = "未获取到开始时间"
    try:
        activity_info['link'] = re.search(r'<a href="(.*?)">', div_content).group(1).strip()
    except AttributeError:
        activity_info['link'] = "未获取到活动链接"
    if activity_info['status'] != '未开始':
        break
    all_activities.append(activity_info)

# 打印输出所有活动信息
print("{\"contests\":" )
print(all_activities)
print("}")
