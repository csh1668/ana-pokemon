# FROM node:20-alpine

# WORKDIR /app

# COPY package*.json ./

# RUN npm install

# COPY . .

# EXPOSE 8091

# CMD ["npm", "start"]

FROM node:20.11-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM node:20.11-alpine

RUN npm install -g serve

COPY --from=build /app/build /app/build

EXPOSE 8091

CMD ["serve", "-s", "/app/build", "-l", "8091"]