import json
import requests
import sys

url = "https://www.luogu.com.cn/api/rating/elo?user=" + sys.argv[1] + "&page=1&limit=50"

payload = {}
headers = {
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'www.luogu.com.cn',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

data_dict = json.loads(response.text)
if data_dict["records"]["result"] == []:
    print("0")
else:
    first_rating = data_dict["records"]["result"][0]["rating"]
    print(first_rating)
