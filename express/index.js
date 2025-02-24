import express from 'express'
import pg from 'pg'
import morgan from 'morgan'

const { Client, Pool } = pg;

const client = new Pool({
    host: 'db',
    port: 5432,
    user: 'root',
    password: 'password',
    database: 'root',
    max: 20,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 2000,
    allowExitOnIdle: false
})

client.connect();

const app = express();
app.use(express.json())
app.use(morgan('dev'))

app.get('/names', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM name_basics")
        res.json(result.rows)
    } catch (err) {
        res.status(500).send(err)
    }
});

app.get('/titles', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM title_basics")
        res.json(result.rows)
    } catch (err) {
        res.status(500).send(err)
    }
})

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});