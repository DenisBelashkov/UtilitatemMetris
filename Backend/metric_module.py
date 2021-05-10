from module import Module
from flask import request, jsonify


class MetricModule(Module):

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics

		@app.route('/metric/update/', methods=["PUT"])
		def update_metric():
			try:
				print(request.json)
				d = request.json
				metric = db.session.query(Metrics).join(Flat,
														Flat.id_personal_account == Metrics.id_personal_account).filter(
					Flat.id_owner_user == d['idUser']).filter(Metrics.id_metrics == d['id']).one_or_none()
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

		@app.route('/metric/<flat_id>', methods=["GET"])
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
					res_list = []
					for m in metrics:
						res_list.append({"id": m.id,
										 "balance": float(m.balance),
										 "typeMetric": m.typeMetric,
										 "prevValue": float(m.prev_value),
										 "currValue": float(m.curr_value),
										 "tariff": float(m.tariff)})
					return jsonify(res_list)
			except Exception as e:
				return " ", 404
