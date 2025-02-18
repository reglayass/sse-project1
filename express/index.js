import express from 'express'
const app = express();

app.get('/', (req, res) => {
    res.send('Hello from Dockerized Express.js!');
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});