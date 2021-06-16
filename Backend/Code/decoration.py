from flask import request

def wrapper_for_token(func):
	def wrap(*args, **kwargs):
		if "token" not in request.headers:
			return " ", 403
		return func(*args, **kwargs)
	wrap.__name__ = func.__name__
	return wrap