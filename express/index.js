import express from 'express'
import pg from 'pg'

const { Client } = pg;

const client = new Client({
    host: 'db',
    port: 5432,
    user: 'root',
    password: 'password',
    database: 'root'
})

client.connect();

const app = express();
app.use(express.json())

app.get('/names', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM name_basics LIMIT 5")
        res.json(result.rows)
    } catch (err) {
        res.status(500).send(err)
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});