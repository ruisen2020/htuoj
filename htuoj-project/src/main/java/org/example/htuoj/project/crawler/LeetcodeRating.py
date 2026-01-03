import requests
import sys
data = {
    "query": "\n    query userContestRankingInfo($userSlug: String!) {\n  userContestRanking(userSlug: $userSlug) {\n    attendedContestsCount\n    rating\n    globalRanking\n    localRanking\n    globalTotalParticipants\n    localTotalParticipants\n    topPercentage\n  }\n  userContestRankingHistory(userSlug: $userSlug) {\n    attended\n    totalProblems\n    trendingDirection\n    finishTimeInSeconds\n    rating\n    score\n    ranking\n    contest {\n      title\n      titleCn\n      startTime\n    }\n  }\n}\n     ",
    "variables": {
        "userSlug": sys.argv[1]
    },
    "operationName": "userContestRankingInfo"
}
response = requests.post('https://leetcode.cn/graphql/noj-go/', json=data)
# print(response.json())
rating = response.json()['data']['userContestRanking']['rating']
print(rating)
