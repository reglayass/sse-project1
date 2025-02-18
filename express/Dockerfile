# Use official Node.js image
FROM node:18-alpine

# Set working directory
WORKDIR /express-app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy application files
COPY . .

# Expose the port
EXPOSE 3000

# Start the application
CMD ["node", "index.js"]