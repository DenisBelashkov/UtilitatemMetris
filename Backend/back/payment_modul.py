from module import Module
from flask import request, jsonify
from datetime import date, datetime
from decoration import wrapper_for_token
from sqlalchemy import and_
import jwt

class PaymentModule(Module):

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Payment = base.classes.Payment_history
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics
		Users = base.classes.Users

		@app.route('/payment/history', methods=["GET"])
		@wrapper_for_token
		def payment_history():
			try:
				r_json = request.json
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				#decode_token = {"id": 2, "email": "milo2", "type": "user"}
				if decode_token["type"] == "demo":
					return " ", 403
				id_user = decode_token["id"]
				res_list = []
				date_1 = datetime.strptime(r_json["dateWith"], '%d-%m-%Y').date()
				date_2 = datetime.strptime(r_json["dateTo"], '%d-%m-%Y').date()
				payments = db.session.query(Payment, Metrics.identifier, Users).join(Metrics, Metrics.id_metrics == Payment.id_metrics)\
					.join(Flat, and_(Flat.id_personal_account == Metrics.id_personal_account, Flat.id_owner_user == id_user))\
					.join(Type_metric, and_(Type_metric.id_type == Metrics.id_type, Type_metric.name == r_json["typeMetric"]))\
					.join(Users, Users.id_user == Payment.id_user)\
					.filter(Payment.date > date_1).filter(Payment.date < date_2).all()
				if len(payments) == 0:
					return " ", 404
				for payment in payments:
					res_list.append(
						{"date": payment.Payment_history.date,
						 "cost": float(payment.Payment_history.cost),
						 "prevVaule": float(payment.Payment_history.prev_value),
						 "currValue": float(payment.Payment_history.curr_value),
						 "userName": payment.Users.email,
						 "identifier": payment.identifier,
						 "id": payment.Payment_history.id_payment_history})
				return jsonify(res_list)
			except Exception as e:
				print(e)
				return "", 500

		# @app.route('/payment/metrics', methods=["POST"])
		# @wrapper_for_token
		# def payment_metrics():
		# 	try:
		# 		print(request.json)
		# 		res_list = []
		# 		json_request = request.json
		# 		for item in json_request:
		# 			metric = db.session.query(Metrics, Tariff.price)\
		# 				.join(Tariff,Tariff.id_tariff == Metrics.id_tariff) \
		# 				.filter(Metrics.id_metrics == item['idMetric']).one_or_none()
		# 			if not metric:
		# 				continue
		# 			cost = item["cost"] + metric.Metrics.balance
		# 			payment = Payment()
		# 			payment.id_metrics = metric.Metrics.id_metrics
		# 			payment.id_user = item["idUser"]
		# 			payment.prev_value = metric.Metrics.prev_value
		# 			payment.date = date.today()
		# 			payment.cost = item["cost"]
		# 			if cost <= 0:
		# 				metric.Metrics.balance = cost
		# 				payment.curr_value = metric.Metrics.prev_value
		# 			else:
		# 				need_cost = (metric.Metrics.curr_value - metric.Metrics.prev_value) * metric.price
		# 				metric.Metrics.balance = cost - need_cost
		# 				payment.curr_value = metric.Metrics.curr_value
		# 				metric.Metrics.prev_value = metric.Metrics.curr_value
		# 			db.session.add(payment)
		# 			db.session.commit()
		# 			res_list.append(
		# 				{"id": payment.id_payment_history,
		# 				 "date": payment.date,
		# 				 "cost": float(payment.cost),
		# 				 "prevValue": float(payment.prev_value),
		# 				 "currValue": float(payment.curr_value),
		# 				 "userName": " "})
		# 		return jsonify(res_list)
		# 	except Exception as e:
		# 		db.session.rollback()
		# 	return "", 400
		def make_payment(id_user, id_metric, cost, prev_value, curr_value)->Payment:
			payment = Payment()
			payment.id_metrics = id_metric
			payment.id_user = id_user
			payment.prev_value = prev_value
			payment.curr_value = curr_value
			payment.date = date.today()
			payment.cost = cost
			return payment

		def serializable_payment(payments, email):
			res_list = []
			for payment in payments:
				res_list.append({"date": payment[0].date,
							"cost": float(payment[0].cost),
							"prevVaule": float(payment[0].prev_value),
							"currValue": float(payment[0].curr_value),
							"userName": email,
							"identifier": payment[1],
							"id": payment[0].id_payment_history})
			return res_list

		@app.route('/payment/metrics', methods=["POST"])
		#@wrapper_for_token
		def post_payment_metrics():
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				#decode_token = {"id": 3, "email" :"milo"}
				r_json = request.json
				cost = float(r_json["cost"])
				res_list = []
				for j_metric in r_json["metrics"]:
					metric = db.session.query(Metrics, Tariff.price)\
						.join(Tariff, Tariff.id_tariff == Metrics.id_tariff)\
						.filter(Metrics.identifier == j_metric["identifier"]).first()
					need_cost = float((metric.Metrics.curr_value - metric.Metrics.prev_value) * metric.price)
					if cost > 0:
						payment: Payment = None
						if metric.Metrics.balance >= 0:
							if metric.Metrics.balance >= need_cost:
								metric.Metrics.balance = float(metric.Metrics.balance) - need_cost
							else:
								balance = float(metric.Metrics.balance)
								metric.Metrics.balance = 0
								#cost += balance
								if cost + balance >= need_cost:
									cost += balance - need_cost
									payment = make_payment(decode_token["id"], metric.Metrics.id_metrics, need_cost - balance, metric.Metrics.prev_value, metric.Metrics.curr_value)
								else:
									payment = make_payment(decode_token["id"], metric.Metrics.id_metrics,
															cost, metric.Metrics.prev_value,
															metric.Metrics.curr_value)
									metric.Metrics.balance = cost + balance - need_cost
									cost = 0
						else:
							need_cost -= float(metric.Metrics.balance)
							if cost >= need_cost:
								cost -= need_cost
								metric.Metrics.balance = 0
								payment = make_payment(decode_token["id"],
														metric.Metrics.id_metrics,
														need_cost,
														metric.Metrics.prev_value,
														metric.Metrics.curr_value)
							else:
								payment = make_payment(decode_token["id"],
														metric.Metrics.id_metrics,
														cost,
														metric.Metrics.prev_value,
														metric.Metrics.curr_value)
								metric.Metrics.balance = cost - need_cost
								cost = 0
						if payment:
							res_list.append((payment, j_metric["identifier"]))
							db.session.add(payment)
					else:
						metric.Metrics.balance = float(metric.Metrics.balance) - need_cost
					metric.Metrics.prev_value = metric.Metrics.curr_value
				if cost > 1:
					db.session.rollback()
					return " ", 406
				db.session.commit()
				return jsonify(serializable_payment( res_list, decode_token["email"]))
			except Exception as e:
				db.session.rollback()
				print(e)
				return " ", 500
