#pragma once

#include "Visitor.h"
#include "Composite.h"
#include "Leaf.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example6
				{
					///
					/// <summary> * ����ķ����ߣ�ʵ�֣������������ƣ�����϶��������ǰ�����"�ڵ㣺"��
					/// * ��Ҷ�Ӷ��������ǰ�����"Ҷ�ӣ�" </summary>
					///
					class PrintNameVisitor : public Visitor
					{
					public:
						virtual void visitComposite(Composite *composite);
						virtual void visitLeaf(Leaf *leaf);
					};

				}
			}
		}
	}
}