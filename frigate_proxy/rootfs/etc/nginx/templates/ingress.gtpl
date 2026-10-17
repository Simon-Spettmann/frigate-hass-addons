server {
    listen 5000 default_server;

    include /etc/nginx/includes/server_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass {{ .server }};
        proxy_set_header X-Ingress-Path {{ .entry }};

        {{ if .proxy_pass_host }}
          proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        {{/* (Optional) Proxy authentication headers */}}
        {{ if and .proxy_user .proxy_role }}
          proxy_set_header X-Proxy-User "{{ .proxy_user }}";
          proxy_set_header X-Proxy-Role "{{ .proxy_role }}";
          {{ if .proxy_secret }}
          proxy_set_header X-Proxy-Secret "{{ .proxy_secret }}";
          {{ end }}
        {{ end }}

        include /etc/nginx/includes/proxy_params.conf;
    }
}
