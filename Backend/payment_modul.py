from module import Module
from flask import request, jsonify
from datetime import date
import datetime


class PaymentModule(Module):

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Payment = base.classes.Payment_history
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff

		@app.route('/payment/history', methods=["GET"])
		def payment_history():
			try:
				print(request.json)
				r_json = request.json
				id = int(r_json["idUser"])
				res_list = []
				date_1 = datetime.strptime(r_json["dateWith"], '%d-%m-%Y').date()
				date_2 = datetime.strptime(r_json["dateTo"], '%d-%m-%Y').date()
				all_m = db.session.query(Payment).join(Metrics, Metrics.id_metrics == Payment.id_metrics)\
					.join(Flat,Flat.id_personal_account == Metrics.id_personal_account)\
					.filter(Flat.id_owner_user == id).filter(Payment.date > date_1).filter(Payment.date < date_2).all()
				for p in all_m:
					res_list.append({"date": p.date, "cost": float(p.cost)})
				return jsonify(res_list)
			except Exception as e:
				return "", 404

		@app.route('/payment/metrics', methods=["POST"])
		def payment_metrics():
			try:
				print(request.json)
				res_list = []
				json_request = request.json
				for item in json_request:
					metric = db.session.query(Metrics, Tariff.price)\
						.join(Tariff,Tariff.id_tariff == Metrics.id_tariff) \
						.filter(Metrics.id_metrics == item['idMetric']).one_or_none()
					if not metric:
						continue
					cost = item["cost"] + metric.Metrics.balance
					payment = Payment()
					payment.id_metrics = metric.Metrics.id_metrics
					payment.id_user = item["idUser"]
					payment.prev_value = metric.Metrics.prev_value
					payment.date = date.today()
					payment.cost = item["cost"]
					if cost <= 0:
						metric.Metrics.balance = cost
						payment.curr_value = metric.Metrics.prev_value
					else:
						need_cost = (metric.Metrics.curr_value - metric.Metrics.prev_value) * metric.price
						metric.Metrics.balance = cost - need_cost
						payment.curr_value = metric.Metrics.curr_value
						metric.Metrics.prev_value = metric.Metrics.curr_value
					db.session.add(payment)
					db.session.commit()
					res_list.append(
						{"id": payment.id_payment_history,
						 "date": payment.date,
						 "cost": float(payment.cost),
						 "prevValue": float(payment.prev_value),
						 "currValue": float(payment.curr_value),
						 "userName": " "})
				return jsonify(res_list)
			except Exception as e:
				db.session.rollback()
			return "", 400
