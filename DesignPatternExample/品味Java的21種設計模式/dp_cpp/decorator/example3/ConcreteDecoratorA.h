#pragma once

#include "Decorator.h"
#include "Component.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example3
				{

					///
					/// <summary> * װ�����ľ���ʵ�ֶ���������������ְ�� </summary>
					/// 
					class ConcreteDecoratorA : public Decorator
					{
					public:
						ConcreteDecoratorA(Component *component);
					///	
					///	 <summary> * ��ӵ�״̬ </summary>
					///	 
					private:
						std::string addedState;

					public:
						virtual std::string getAddedState();

						virtual void setAddedState(std::string addedState);

						virtual void operation();
					};


				}
			}
		}
	}
}