import requests

url = "https://www.luogu.com.cn/contest/list?page=1&_contentOnly=1"

payload={}
headers = {
    'priority': 'u=0, i',
    'sec-fetch-user': '?1',
    'upgrade-insecure-requests': '1',
    'Cookie': '__client_id=4f3dc0325ae7c1922ab88c9ed49eef120d4cbeda; _uid=451563; C3VK=6635a2',
    'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
    'Accept': '*/*',
    'Host': 'www.luogu.com.cn',
    'Connection': 'keep-alive'
}

response = requests.request("GET", url, headers=headers, data=payload)

print(response.text)
