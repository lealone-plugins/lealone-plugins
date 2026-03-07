create config lealone (
    base_dir: '${r'${LEALONE_HOME}'}/data',
    protocol_server_engine: (
        name: 'tomcat',
        enabled: true,
        port: 8080,
        web_root: '../web',
        jdbc_url: 'jdbc:lealone:embed:${dbName}?user=root&password=',
        router: '${packageName}.web.${appClassName}Router'
    )
)
