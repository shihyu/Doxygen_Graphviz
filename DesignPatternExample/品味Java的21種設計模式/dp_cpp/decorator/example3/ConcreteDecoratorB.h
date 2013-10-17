#pragma once

#include "Decorator.h"
#include "Component.h"

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
					class ConcreteDecoratorB : public Decorator
					{
					public:
						ConcreteDecoratorB(Component *component);
					///	
					///	 <summary> * ��Ҫ��ӵ�ְ�� </summary>
					///	 
					private:
						void addedBehavior();
					public:
						virtual void operation();
					};


				}
			}
		}
	}
}