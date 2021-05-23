from module import Module
from flask import request, jsonify
import jwt
from decoration import wrapper_for_token

class MetricModule(Module):

	@staticmethod
	def make_json(metrics):
		res_list = []
		for m in metrics:
			res_list.append({"id": m.id,
							 "balance": float(m.balance),
							 "typeMetric": m.typeMetric,
							 "prevValue": float(m.prev_value),
							 "currValue": float(m.curr_value),
							 "tariff": float(m.tariff)})
		return res_list

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics

		@app.route('/metric/update/', methods=["PUT"])
		@wrapper_for_token
		def update_metric():
			try:
				print(request.json)
				token = request.headers["token"]
				token_decode = jwt.decode(token, "secret", algorithms=["HS256"])
				r_json = request.json
				metric = db.session.query(Metrics).join(Flat,
														Flat.id_personal_account == Metrics.id_personal_account).filter(
					Flat.id_owner_user == token_decode["id"]).filter(Metrics.id_metrics == r_json['id']).one_or_none()
				if not metric:
					return "", 404
				if metric.curr_value > r_json['currentValue']:
					return "", 400
				metric.curr_value = r_json['currentValue']
				db.session.commit()
				return "", 200
			except Exception as e:
				db.session.rollback()
			return "", 400

		@app.route('/metric/byUserId/<user_id>', methods = ["GET"])
		@wrapper_for_token
		def get_all_metric_of_user(user_id):
			try:
				id = int(user_id)
				metrics = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric")) \
					.join(Tariff, Tariff.id_tariff == Metrics.id_tariff) \
					.join(Type_metric, Type_metric.id_type == Metrics.id_type) \
					.join(Flat, Flat.id_personal_account == Metrics.id_personal_account) \
					.filter(Flat.id_owner_user == id).all()
				if len(metrics) > 0:
					return jsonify(self.make_json(metrics))
			except Exception as e:
				return " ", 404

		@app.route('/metric/byFlatId/<flat_id>', methods=["GET"])
		@wrapper_for_token
		def get_metrics_by_id_flat(flat_id):
			try:
				id = int(flat_id)
				metrics = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric"))\
					.join(Tariff,Tariff.id_tariff == Metrics.id_tariff)\
					.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
					.filter(Metrics.id_personal_account == id).all()
				if len(metrics) > 0:
					return jsonify(self.make_json(metrics))
			except Exception as e:
				return " ", 404

		@app.route('/metric/<identifier>', methods=["GET"])
		@wrapper_for_token
		def get_metric_identifier(identifier):
			try:
				metric = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric"))\
					.join(Tariff,Tariff.id_tariff == Metrics.id_tariff)\
					.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
					.filter(Metrics.identifier == identifier).all()
				if len(metric) > 0:
					return jsonify(self.make_json(metric))
			except Exception as e:
				return " ", 404