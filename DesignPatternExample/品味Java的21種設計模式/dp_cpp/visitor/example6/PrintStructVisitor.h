#pragma once

#include "Visitor.h"
#include "Composite.h"
#include "Leaf.h"
#include <string>

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
					/// <summary> * ����ķ����ߣ�ʵ�֣������϶�������Ľṹ </summary>
					///
					class PrintStructVisitor : public Visitor
					{
					///	
					///	 <summary> * �����ۼƼ�¼������Ҫ����˵ĸ� </summary>
					///	 
					private:
						std::string preStr;
					public:
						virtual void visitComposite(Composite *composite);
						virtual void visitLeaf(Leaf *leaf);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								preStr = "";

								initialized = true;
							}
						}

public:
	PrintStructVisitor()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}