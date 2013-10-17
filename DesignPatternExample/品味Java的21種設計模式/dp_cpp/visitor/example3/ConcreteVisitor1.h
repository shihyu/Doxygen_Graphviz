#pragma once

#include "Visitor.h"
#include "ConcreteElementA.h"
#include "ConcreteElementB.h"

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
					/// <summary> * ����ķ�����ʵ�� </summary>
					/// 
					class ConcreteVisitor1 : public Visitor
					{
					public:
						virtual void visitConcreteElementA(ConcreteElementA *element);
						virtual void visitConcreteElementB(ConcreteElementB *element);
					};
				}
			}
		}
	}
}