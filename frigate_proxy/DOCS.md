This addon creates a proxy to a Frigate server run separately from Home Assistant so that you can have the benefit of access in the sidebar without running Frigate as an addon.

Note that this addon does not run Frigate itself.

## Configuration

### Option: `server`

The `server` option sets the address of the Frigate server.

This must be in the format `http[s]://host:port`. The following are valid examples:

- `http://frigate.local:5000`
- `http://192.168.0.101:5000`
- `https://192.168.0.101:443`

### Option: `proxy_pass_host`

Determines whether we should pass the host we're running on (for example,
`homeassistant.local`) to the server we're proxying to. In general, you probably
want this to be set to `true`.

Set to `false` if the server needs to receive the host of the frigate instance
(not the host Home Assistant or this addon are running on). This might be the case
if your frigate instance is behind an SSL proxy (like Traefik or Caddy), which
needs to receive the frigate host in order to route the request correctly.

### Option: `proxy_pass_real_ip`

Determines whether we should pass the client's real IP address to the server we're proxying to. In general, you probably
want this to be set to `true`.

Set to `false` if you need to know the request is coming from the HA IP. This might be the case if your frigate instance is behind a proxy which only allows specific IPs to connect.

### Option: `proxy_user` (optional)

The username to use for proxy authentication. When set along with `proxy_role`, the addon will pass this as the `X-Proxy-User` header to Frigate.

### Option: `proxy_role` (optional)

The role to assign for proxy authentication. When set along with `proxy_user`, the addon will pass this as the `X-Proxy-Role` header to Frigate. Valid roles are those defined in your Frigate configuration.

### Option: `proxy_secret` (optional)

A secret key for validating proxy requests. When set, along with `proxy_user` and `proxy_role`, the addon will pass this as the `X-Proxy-Secret` header to Frigate. Frigate will validate this secret before accepting proxy authentication headers. This is optional but strongly recommended for security.

## Proxy Authentication

This addon supports optional proxy authentication to enable seamless access to Frigate through Home Assistant (no need to log in separately). When configured, the addon passes authentication headers to Frigate, which can validate these headers using its built-in proxy authentication system.

### How it works

When `proxy_user` and `proxy_role` are set:

1. The addon passes authentication headers (`X-Proxy-User`, `X-Proxy-Role`, and (if set) `X-Proxy-Secret`) to Frigate
2. Frigate validates these headers using its proxy configuration
3. Users accessing through Home Assistant are automatically authenticated with the specified role

When only `proxy_user` or `proxy_role` or neither are set:

- No headers are sent (maintains backward compatibility)

### Requirements

In your Frigate `config.yml`, add or set:

```yaml
auth:
  enabled: False

proxy:
  header_map:
    user: X-Proxy-User
    role: X-Proxy-Role
  default_role: viewer
  auth_secret: "strong-secret-key"  # Must match the addon's proxy_secret if set
```

### Considerations

- Disabling Frigate's authentication will disable access through the login page. To login as another user (e.g. admin) you will need to either:
  - Change `proxy_user` and/or `proxy_role`
  - Use another proxy (with e.g. Basic/Digest Auth)
- The configuration options can be set and seen by all Home Assistant administrators - They could use the `auth_secret` to gain full control of Frigate!

## Required Dependencies

- Network access to running Frigate server

## Support

Please [open an issue](https://github.com/blakeblackshear/frigate/issues/new/choose) if you need support.
