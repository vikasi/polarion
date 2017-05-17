set PATH=C:\Polarion\bundled\svn\bin;C:\Polarion\bundled\java\bin;C:\Polarion\bundled\apache\bin;%PATH%

start "Apache Server" "C:\Polarion\polarion shortcuts\Start Apache Service.lnk"
start "PostgreSQLPolarion Service" "C:\Polarion\polarion shortcuts\Start PostgreSQL Service.lnk"
start "Polarion Server" "C:\Polarion\polarion shortcuts\Start Polarion Server.lnk"
