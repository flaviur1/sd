FROM node:20
WORKDIR /app
RUN npm install -g serve
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 80
CMD ["serve", "-s", "dist", "-l", "80"]