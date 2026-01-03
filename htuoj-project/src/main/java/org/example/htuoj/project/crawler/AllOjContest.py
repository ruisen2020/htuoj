import requests
import sys
import io

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='UTF-8')
url = "https://github.moeyy.xyz/https://raw.githubusercontent.com/mqnu00/ACM-contest-calender-maker/refs/heads/main/contest.json"

payload={}
headers = {
    'priority': 'u=0, i',
    'sec-fetch-user': '?1',
    'upgrade-insecure-requests': '1',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'github.moeyy.xyz',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)
