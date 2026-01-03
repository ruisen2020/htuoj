import re
import requests
import sys

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36',
    'Content-Type': 'application/json'
}
response = requests.get('https://www.luogu.com.cn/user/'+sys.argv[1],headers=headers)
# print(response.text)
text_content = response.text

# text = "passedProblemCount%22%3A405%2C%22"
match = re.search(r'passedProblemCount%22%3A(\d+)%2C%22', text_content)
if match:
    number = match.group(1)
    print(number)
else:
    print("")
