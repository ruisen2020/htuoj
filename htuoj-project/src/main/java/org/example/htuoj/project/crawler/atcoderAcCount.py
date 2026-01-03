import json
import requests
import sys

url = "https://kenkoooo.com/atcoder/atcoder-api/v3/user/ac_rank?user=" + sys.argv[1]

payload = {}
headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'kenkoooo.com',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)
data = response.text
data_dict = json.loads(data)
count_value = data_dict['count']
print(count_value)
