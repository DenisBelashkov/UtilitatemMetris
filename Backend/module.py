from abc import ABC, abstractmethod


class Module(ABC):

	@abstractmethod
	def setOptions(self, app):
		pass

