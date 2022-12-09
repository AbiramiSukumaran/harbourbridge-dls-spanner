# Minimal Downtime Migration to Spanner using HarbourBridge) project

Welcome to the harbourbridge-dls-spanner (Minimal Downtime Migration to Spanner using HarbourBridge) project!

In this repo, you will find resources that will help you do the MySQL to Cloud Spanner Migration using HarbourBridge. 
Duckworth Lewis Parscore Application

The use case I have created to discuss to demonstrate this migration is an application that streams in live (near real-time) T20 cricket match data ball-by-ball and calculates the Duckworth Lewis Target Score (also known as the Par Score) for Team 2, second innings, in case the match is disrupted mid-innings due to rain or other circumstances. This is calculated using the famous Duckworth Lewis Stern (DLS) algorithm and gets updated for every ball in the second innings; that way we will always know what the winning target is, in case the match gets interrupted and is not continued thereafter. There are several scenarios in Cricket that use the DLS algorithm for determining the target or winning score. 
Scenario
We will consider one of the scenarios i.e. if the match is stopped mid-second innings at any over and ball, and if Team 1 played their entire innings with 100% resources, then what is considered the winning score for Team 2? We would calculate this score for every ball of the innings to ensure we know live, on the fly, which team is winning.

DLS Par Score Formula for the scenario
Par Score for Team 2 = Team 1’s Runs * (Team 2’s Resources / Team 1’s Resources)
Team’s Resources refer to the amount of resources (in terms of overs and wickets) utilized by a team and measured using a Standard Duckworth Lewis Resource Table, in this case, scaled for T20. There is a professional edition used by international cricket, but I do not have access to it.

DLS Resource Table Standard Edition (scaled for T20)
If you are wondering what a Resource Table is, imagine it being similar to a Logarithmic Table where you look up for the power to which a specific base number must be raised in order to get another number. Similarly, the Resource Table tells you the resources left unutilized by a team looked up by the team’s Overs left and Wickets lost.

Source and Destination DB steps to remember:
1. Install MySQL Server or use Google Cloud SQL for MySQL for the migration
2. Create your source database named "<b>cricket_db</b>" and use the Source DDL to create source schema and objects (3 tables)
3. Execute the insert statements (Source DMLs) to create source data to dls_src and STD_DLS_RESOURCE table
4. Execute the insert statements (Source DMLs) to create initial bulk data to dls table
5. Save the Data Validation SELECT queries for post-migration validation in both MySQL and Spanner to ensure data-sync

