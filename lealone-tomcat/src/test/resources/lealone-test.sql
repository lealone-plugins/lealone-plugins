-- Copyright Lealone Database Group.
-- Licensed under the Server Side Public License, v 1.
-- Initial Developer: zhh

create config lealone (
    base_dir: 'target/test-data',
    scheduler: (
        scheduler_count: 4,
        prefer_batch_write: false,
        max_packet_count_per_loop: 10, -- 每次循环最多读取多少个数据包，默认20
    ),
    protocol_server_engine: (
        name: 'Tomcat',
        enabled: true,
        port: 8080,
        allow_others: true,
        ssl: false,
        session_timeout: -1,
        web_root: 'src/test/resources/web',
        router: 'com.lealone.plugins.tomcat.test.LealoneTomcatStart'
    )
)
