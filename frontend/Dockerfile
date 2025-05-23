########################
# 1-й этап: сборка SPA #
########################
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build          # → /app/build

############################
# 2-й этап: Nginx-runtime  #
############################
FROM nginx:1.25-alpine
# статические файлы
COPY --from=build /app/build /usr/share/nginx/html
# конфиг виртуального хоста
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx","-g","daemon off;"]
