#pragma once

#include "Visitor.h"
#include "EnterpriseCustomer.h"
#include "PersonalCustomer.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example4
				{
					///
					/// <summary> * ����ķ����ߣ�ʵ�ֶԿͻ���ƫ�÷��� </summary>
					/// 
					class PredilectionAnalyzeVisitor : public Visitor
					{
					public:
						virtual void visitEnterpriseCustomer(EnterpriseCustomer *ec);
						virtual void visitPersonalCustomer(PersonalCustomer *pc);
					};
				}
			}
		}
	}
}