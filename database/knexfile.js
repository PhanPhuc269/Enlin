require('dotenv').config();

module.exports = {
  development: {
    client: 'pg',
    connection: process.env.DATABASE_URL || {
      host: `${process.env.HOST}`,
      port: `${process.env.PORT}`,
      user: `${process.env.USER}`,
      password: `${process.env.PASSWORD}`,
      database: `${process.env.DATABASE}`
    },
    migrations: {
      tableName: 'knex_migrations'
    }
  },
}