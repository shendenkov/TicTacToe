FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY index.html favicon.ico config.js /usr/share/nginx/html/
CMD ["nginx", "-g", "daemon off;"]