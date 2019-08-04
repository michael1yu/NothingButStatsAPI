# NothingButStatsAPI

# Description
This API serves the Nothing But Stats website. It retrieves NBA data from https://www.balldontlie.io and writes it to a Google Firestore Database
so that data queries can be made that are not available through the balldontlie API.

# Endpoints

The API site is nothingbutstatsapi.herokuapp.com


/get_player_info - (params: team and player id) (returns ex: height, weight, position, etc)

/get_player_stats - (params: team and player id) (returns ex: games player, points per game, etc)

/get_player_team - (params: player id) (returns ex: Los Angeles Lakers)

/get_teams - (no params) (returns list of teams)

/query_current_players - (params: team and current) (returns list of all players that match current(boolean) parameter)


# Disclaimer
Database only up to date for 2018-2019 NBA season.
Please do not use this API for official applications as NBA data served by this API will likely be outdated.

Additionally, since the API is hosted on Heroku (free tier) it may take time to start running due to Heroku sleeping projects that have been inactive for ~30 minutes
