# Use Node.js environment
FROM node:20

WORKDIR /app

# Install the "serve" package globally
# This is a simple web server for static files
RUN npm install -g serve

# Copy package files and install app dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the code
COPY . .

# Build the React app (creates the 'dist' folder)
RUN npm run build

# Expose port 80 (to match what Traefik expects)
EXPOSE 80

# Start the server pointing to the 'dist' folder on port 80
CMD ["serve", "-s", "dist", "-l", "80"]