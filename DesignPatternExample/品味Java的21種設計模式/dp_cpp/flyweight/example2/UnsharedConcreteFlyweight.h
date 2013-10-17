#pragma once

#include "Flyweight.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example2
				{

					///
					/// <summary> * ����Ҫ�����Flyweight����
					/// * ͨ���ǽ����������Ԫ������Ϊ�ӽڵ㣬��ϳ����Ķ��� </summary>
					/// 
					class UnsharedConcreteFlyweight : public Flyweight
					{
					///	
					///	 <summary> * ʾ�������������״̬ </summary>
					///	 
					private:
						std::string allState;

					public:
						virtual void operation(std::string extrinsicState);
					};

				}
			}
		}
	}
}