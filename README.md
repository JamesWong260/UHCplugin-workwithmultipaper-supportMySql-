# UHCplugin-workwithmultipaper-supportMySql-Bysiege260(James)
mostly UHCplugin cannot support Cross server or more than one instance(e.g multipaper), so I code
A plugin can save the variable or data in MYSQL not just in the plugin folder. Since the plugin is
in developing, there may be many bugs.

# HOW TO USE
drop into your server's plugin folder and restart the server
then you will see the config file generated in your plugin folder(UHC2023)
fill your MYSQL database's information(IP user password Basename)

!!!caution!!!
you need to create a indivdual database for each minecraft server
and a common database

for example:
server1
  individual SQL base: server1database
  table: commonbase
server2
  individual SQL base: server2database
  table: commonbase
server3
  individual SQL base: server3database
  table: commonbase

next, fill your prepare time, Border size as well as battle time
in preparetime you cannot hit anyone the Border size wouldn't change
after the prepare time, You can start attack another player and the
Border size will be decreased until the radius to 10.(battle time = 
the rate of radius transform)

