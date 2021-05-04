from flask import Flask
from sqlalchemy.ext.automap import automap_base
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://tntrol:password@localhost:3306/mydb'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['CSRF_ENABLED'] = True
app.config['SECRET_KEY'] = 'a really really really really long secret key'

db: SQLAlchemy = SQLAlchemy(app, session_options={'autoflush': False})
Base = automap_base()
Base.prepare(db.engine, reflect=True)

