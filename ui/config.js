const pathParts = window.location.pathname.split('/').filter(Boolean);
const BASE_PATH = pathParts.length > 0 ? `/${pathParts[0]}` : '';
const CONFIG = {
    API_HTTP_BASE_URL: `${window.location.origin}${BASE_PATH}`,
    API_WS_BASE_URL: `${window.location.origin.replace(/^http/, 'ws')}${BASE_PATH}/ws`
};