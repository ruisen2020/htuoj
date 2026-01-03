import requests
import sys

data = {
    "query": "\n    query userProfileUserQuestionProgressV2($userSlug: String!) {\n  userProfileUserQuestionProgressV2(userSlug: $userSlug) {\n    numAcceptedQuestions {\n      count\n      difficulty\n    }\n    numFailedQuestions {\n      count\n      difficulty\n    }\n    numUntouchedQuestions {\n      count\n      difficulty\n    }\n    userSessionBeatsPercentage {\n      difficulty\n      percentage\n    }\n    totalQuestionBeatsPercentage\n  }\n}\n    ",
    "variables": {
        "userSlug": sys.argv[1]
    },
}

response = requests.post('https://leetcode.cn/graphql/', json=data)
# print(response.json())
easy = response.json()['data']['userProfileUserQuestionProgressV2']['numAcceptedQuestions'][0]['count']
medium = response.json()['data']['userProfileUserQuestionProgressV2']['numAcceptedQuestions'][1]['count']
hard = response.json()['data']['userProfileUserQuestionProgressV2']['numAcceptedQuestions'][2]['count']
print(easy + medium + hard)
