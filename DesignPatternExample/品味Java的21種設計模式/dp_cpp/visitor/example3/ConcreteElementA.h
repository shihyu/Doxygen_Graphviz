#pragma once

#include "Element.h"
#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example3
				{
					///
					/// <summary> * ����Ԫ�ص�ʵ�ֶ��� </summary>
					/// 
					class ConcreteElementA : public Element
					{
					public:
						virtual void accept(Visitor *visitor);
					///	
					///	 <summary> * ʾ����������ʾԪ�����еĹ���ʵ�� </summary>
					///	 
						virtual void opertionA();
					};


				}
			}
		}
	}
}