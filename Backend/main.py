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
Payment = Base.classes.Payment_history

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

@app.route('/payment/metrics', methods=["POST"])
def payment_metrics():
	try:
		print(request.json)
		json_r = request.json
		for d in json_r:
			metric = db.session.query(Metrics).join(Tariff, Tariff.id_tariff == Metrics.id_tariff).filter(Metrics.id_metrics == d['idMetric']).one_or_none()
			cost = d["cost"]
			need_cost = metric.curr_value - metric.prev_value
			print(1)
		return "", 200
	except Exception as e:
		pass
	return "", 400

@app.route('/metric/update/', methods=["PUT"])
def update_metric():
	try:
		print(request.json)
		d = request.json
		metric = db.session.query(Metrics).join(Flat, Flat.id_personal_account == Metrics.id_personal_account).filter(Flat.id_owner_user == d['idUser']).filter(Metrics.id_metrics == d['id']).one_or_none()
		if not metric:
			return "", 404
		if metric.curr_value > d['currentValue']:
			return "", 400
		metric.curr_value = d['currentValue']
		db.session.commit()
		return "", 200
	except Exception as e:
		db.session.rollback()
	return "", 400

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
		id = int(user_id)
		flats = db.session.query(Flat.id_personal_account, Address).join(Address, Address.id_adress == Flat.id_adress).filter(Flat.id_owner_user == id).all()
		if len(flats) > 0:
			out = []
			for f in flats:
				out.append({"id" : f.id_personal_account, "address" : toString(f.Address)})
			return jsonify(out)
		return " ", 404
	except Exception as e:
		print(e)
	return " ", 400

@app.route('/metric/<flat_id>', methods=["GET"])
def get_metrics_by_id_flat(flat_id):
	try:
		id = int(flat_id)
		metrics = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value, Metrics.curr_value, Tariff.price.label("tariff"), Type_metric.name.label("typeMetric")).join(Tariff, Tariff.id_tariff == Metrics.id_tariff).join(Type_metric, Type_metric.id_type == Metrics.id_type).filter(Metrics.id_personal_account == id).all()
		if len(metrics) > 0:
			return jsonify(metrics)
	except Exception as e:
		pass
	return " ", 404

if __name__ == '__main__':
	app.wsgi_app = ProxyFix(app.wsgi_app)
	app.run()