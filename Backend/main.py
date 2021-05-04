from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import and_
from werkzeug.middleware.proxy_fix import ProxyFix
from sqlalchemy.ext.automap import automap_base


app = Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://tntrol:password@localhost:3306/mytp'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['CSRF_ENABLED'] = True
app.config['SECRET_KEY'] = 'a really really really really long secret key'

db: SQLAlchemy = SQLAlchemy(app, session_options={'autoflush': False})
Base = automap_base()
Base.prepare(db.engine, reflect=True)

Users = Base.classes.Users
Metrics = Base.classes.Metrics
Flat = Base.classes.Personal_account
Type_metric = Base.classes.Type_metrics
Tariff = Base.classes.Tariff
Address = Base.classes.Address


@app.route('/', methods=["GET"])
def hello_world():
	#print(db.session.query(Users).all())
	return 'Hello, World!\n'

@app.route('/app/', methods=["GET"])
def hello():
	print(request.values['id'])

	return 'Hello, World2!\n'

@app.route('/json/', methods=["POST"])
def hworld():
	print(request.json)
	return 'Hello, World!3\n'

@app.route('/json2/', methods=["GET"])
def heworld():
	try:
		print(request.json['ema'])
	except Exception as e:
		print('all is bad')
	return 'Hello, World!4\n'

@app.route('/put/', methods=["PUT"])
def pworld():
	print(request.json)
	return 'Hello, World!5\n'

def toString(a):
	str = ""
	while True:
		str += a.name
		if not a.id_include:
			return str
		a = a.address

@app.route('/flat/<user_id>', methods=["GET"])
def get_flats_by_id_user(user_id):
	try:
		print(user_id)
		id = int(user_id)
		flats = db.session.query(Flat.id_personal_account, Address).join(Address, Address.id_adress == Flat.id_adress).filter(Flat.id_owner_user == id).all()
		if len(flats) > 0:
			out = []
			for f in flats:
				out.append({"id" : f.id_personal_account, "address" : toString(f.Address)})
			return jsonify(out)
		return " ", 404
	except Exception as e:
		print (e.__str__())
	return " ", 400

@app.route('/metric/<flat_id>', methods=["GET"])
def get_metrics_by_id_flat(flat_id):
	try:
		id = int(flat_id)
		metrics = db.session.query(Metrics.id_metrics, Metrics.balance, Metrics.prev_value, Metrics.curr_value, Tariff.price, Type_metric.name).join(Tariff, Tariff.id_tariff == Metrics.id_tariff).join(Type_metric, Type_metric.id_type == Metrics.id_type).filter(Metrics.id_personal_account == id).all()
		if len(metrics) > 0:
			return metrics
	except Exception as e:
		pass
	return " ", 404

if __name__ == '__main__':
	app.wsgi_app = ProxyFix(app.wsgi_app)
	app.run()